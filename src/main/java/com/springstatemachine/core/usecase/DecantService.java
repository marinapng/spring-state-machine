package com.springstatemachine.core.usecase;

import com.springstatemachine.core.domain.Decant;
import com.springstatemachine.core.domain.statemachine.DecantEvent;
import com.springstatemachine.core.domain.statemachine.DecantState;
import com.springstatemachine.core.repository.DecantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;

import static com.springstatemachine.core.domain.statemachine.DecantEvent.START_DECANT;
import static com.springstatemachine.core.domain.statemachine.DecantEvent.START_ERROR_DECANT;
import static com.springstatemachine.core.domain.statemachine.DecantState.PENDING;

@RequiredArgsConstructor
public class DecantService {

    private final DecantRepository decantRepository;
    private final StateMachineFactory<DecantState, DecantEvent> statemachineFactory;

    public Decant createDecant(Decant decant) {
        decant.setState(PENDING);
        return decantRepository.save(decant);
    }

    public StateMachine<DecantState, DecantEvent> startDecant(Decant decant) {
        StateMachine<DecantState, DecantEvent> stateMachine = buildStateMachine(decant.getId());
        if(ObjectUtils.isEmpty(decant.getId())){
            sendEvent(decant.getId(), stateMachine, START_DECANT);
            return stateMachine;
        }
        sendEvent(decant.getId(), stateMachine, START_ERROR_DECANT);
        return stateMachine;
    }

    private void sendEvent(Long decantId, StateMachine<DecantState, DecantEvent> stateMachine, DecantEvent event) {
        Message<DecantEvent> message = MessageBuilder.withPayload(event)
                .setHeader("decant id", decantId)
                .build();

        stateMachine.sendEvent(message);
    }

    private StateMachine<DecantState, DecantEvent> buildStateMachine(Long decantId) {
        Decant decant = decantRepository.getById(decantId);

        StateMachine<DecantState, DecantEvent> stateMachine = statemachineFactory.getStateMachine(Long.toString(decant.getId()));

        stateMachine.stopReactively();

        /*
         * Create state machine with current state from repository
         */
        stateMachine.getStateMachineAccessor().doWithAllRegions(stateM -> {
            stateM.resetStateMachineReactively(new DefaultStateMachineContext<>(decant.getState(), null, null, null));
        });

        stateMachine.startReactively();
        return stateMachine;


    }
}
