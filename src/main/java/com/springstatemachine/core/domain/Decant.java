package com.springstatemachine.core.domain;

import com.springstatemachine.core.domain.statemachine.DecantState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Decant {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private DecantState state;


}
