package com.swanhtetaungphyo.article_approval.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InternalResponse {
    private String rejectionMessage;
    private boolean condition;

    public boolean getCondition(){
        return  this.condition;
    }
}
