/*
 * Copyright (c) Fundacion Jala. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 * Added By Rahul Agrawal: to Check Parameter Count in Methods/Constructor, It should not be more than 5 Parameters
 */
package org.fundacionjala.enforce.sonarqube.apex.checks.unofficial;

import org.sonar.squidbridge.checks.SquidCheck;

import org.sonar.check.Rule;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

import org.fundacionjala.enforce.sonarqube.apex.api.grammar.ApexGrammarRuleKey;
import org.fundacionjala.enforce.sonarqube.apex.checks.ChecksBundle;
import org.fundacionjala.enforce.sonarqube.apex.checks.ChecksLogger;

/**
 * Verification of the name of the method in a class.
 */
@Rule(key = ParameterCountCheck.CHECK_KEY)
public class ParameterCountCheck extends SquidCheck<Grammar> {

    /**
     * It is the code of the rule for the plugin.
     */
    public static final String CHECK_KEY = "ParameterCount";

    /**
     * The structure must have the name of the method.
     */
    public final Integer DEFAULT_PARAM_COUNT = 5;

    private final String MESSAGE = ChecksBundle.getStringFromBundle("ParameterCountCheckMessage");

    

    /**
     * The variables are initialized and subscribe the base rule.
     */
    
    @Override
    public void init() {
        subscribeTo(ApexGrammarRuleKey.METHOD_DECLARATION, ApexGrammarRuleKey.CONSTRUCTOR_DECLARATION);
    }

    /**
     * It is responsible for verifying whether the rule is met in the rule base.
     * In the event that the rule is not correct, create message error.
     *
     * @param astNode It is the node that stores all the rules.
     */
    @Override
    public void visitNode(AstNode astNode) {
        try {
            LookForParameterCount(astNode);
        } catch (Exception e) {
            ChecksLogger.logCheckError(this.toString(), "visitNode", e.toString());
        }
    }
    
    private void LookForParameterCount(AstNode methodDeclarationNode) {
        if(methodDeclarationNode.getDescendants(ApexGrammarRuleKey.FORMAL_PARAMETER).size() > DEFAULT_PARAM_COUNT){
	        getContext().createLineViolation(this, MESSAGE, methodDeclarationNode, DEFAULT_PARAM_COUNT.toString());
        }
        
    }
}