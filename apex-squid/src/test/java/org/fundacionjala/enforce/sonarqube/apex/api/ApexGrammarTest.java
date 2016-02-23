/*
 * The MIT License
 *
 * Copyright 2016 Fundacion Jala.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.fundacionjala.enforce.sonarqube.apex.api;

import org.junit.Test;

import com.sonar.sslr.api.Grammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

import static org.fundacionjala.enforce.sonarqube.apex.api.grammar.RuleKey.APEX_GRAMMAR;

public class ApexGrammarTest {

    private final Grammar grammarBuilder = ApexGrammar.create(Boolean.FALSE);

    @Test
    public void correctRuleBasic() {
        assertThat(grammarBuilder.rule(APEX_GRAMMAR))
                .matches("publicclassMyClass{publicbooleanMyMethod(){}}");
    }

    @Test
    public void correctRuleMoreImplements() {
        assertThat(grammarBuilder.rule(APEX_GRAMMAR))
                .matches("publicwithsharingclassClass1implementsYourClass{"
                        + "publicbooleanMyMethod(intmyParameter){"
                        + "intnumber=myParameter;"
                        + "}"
                        + "}");
    }

    @Test
    public void correctRuleMoreExtends() {
        assertThat(grammarBuilder.rule(APEX_GRAMMAR))
                .matches("publicwithsharingclassClass1extendsYourClass{"
                        + "publicbooleanMyMethod(){}"
                        + "}");
    }
    
    @Test
    public void correctRuleBasicVariable() {
        assertThat(grammarBuilder.rule(APEX_GRAMMAR))
                .matches("publicclassMyClass{intmyVariable;}");
    }

    @Test
    public void correctRuleMoreImplementsVariableAndMethod() {
        assertThat(grammarBuilder.rule(APEX_GRAMMAR))
                .matches("publicwithsharingclassClass1implementsYourClass{"
                        + "publicbooleanmy_Variable=true;"
                        + "publicbooleanMyMethod(){}"
                        + "}");
    }

    @Test
    public void correctRuleMoreExtendsVariableAndMethod() {
        assertThat(grammarBuilder.rule(APEX_GRAMMAR))
                .matches("publicwithsharingclassClass1extendsYourClass{"
                        + "privateintmy_Variable=10;"
                        + "publicbooleanMyMethod(){}"
                        + "}");
    }
}
