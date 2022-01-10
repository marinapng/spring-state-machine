package com.springstatemachine.infra.config.statemachinespring;

import com.springstatemachine.core.domain.statemachine.DecantEvent;
import com.springstatemachine.core.domain.statemachine.DecantState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import static com.springstatemachine.core.domain.statemachine.DecantEvent.ERROR_IDENTIFY_ITEM;
import static com.springstatemachine.core.domain.statemachine.DecantEvent.ERROR_UPDATE_ITEM;
import static com.springstatemachine.core.domain.statemachine.DecantEvent.FINISH_DECANT;
import static com.springstatemachine.core.domain.statemachine.DecantEvent.IDENTIFY_ITEM;
import static com.springstatemachine.core.domain.statemachine.DecantEvent.REGISTER_DECANT;
import static com.springstatemachine.core.domain.statemachine.DecantEvent.START_DECANT;
import static com.springstatemachine.core.domain.statemachine.DecantEvent.START_ERROR_DECANT;
import static com.springstatemachine.core.domain.statemachine.DecantEvent.UPDATE_ITEM;
import static com.springstatemachine.core.domain.statemachine.DecantState.COMPLETED;
import static com.springstatemachine.core.domain.statemachine.DecantState.PENDING;
import static com.springstatemachine.core.domain.statemachine.DecantState.PROCESSING;


@Slf4j
@EnableStateMachineFactory
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<DecantState, DecantEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<DecantState, DecantEvent> states) throws Exception {
        states.withStates()
                .initial(PENDING)
                .state(PROCESSING)
                .end(COMPLETED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<DecantState, DecantEvent> transitions) throws Exception {
        transitions.withExternal().source(PENDING).target(PENDING).event(START_ERROR_DECANT)
                .and().withExternal().source(PENDING).target(PENDING).event(REGISTER_DECANT)
                .and().withExternal().source(PENDING).target(PROCESSING).event(START_DECANT)
                .and().withExternal().source(PROCESSING).target(PROCESSING).event(IDENTIFY_ITEM)
                .and().withExternal().source(PROCESSING).target(PROCESSING).event(ERROR_IDENTIFY_ITEM)
                .and().withExternal().source(PROCESSING).target(PROCESSING).event(UPDATE_ITEM)
                .and().withExternal().source(PROCESSING).target(PROCESSING).event(ERROR_UPDATE_ITEM)
                .and().withExternal().source(PROCESSING).target(COMPLETED).event(FINISH_DECANT);
    }
}
