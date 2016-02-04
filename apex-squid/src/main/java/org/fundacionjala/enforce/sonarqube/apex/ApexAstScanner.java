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
package org.fundacionjala.enforce.sonarqube.apex;

import java.io.File;
import java.util.Collection;
import com.google.common.base.Charsets;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;
import org.fundacionjala.enforce.sonarqube.apex.api.ApexMetric;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.SquidAstVisitorContextImpl;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.indexer.QueryByType;

/**
 * Utility class for scans a file and generate {@link SourceFile}
 */
public class ApexAstScanner {

    /**
     * Default constructor.
     */
    private ApexAstScanner() {
    }

    /**
     * Returns a scanner from configuration and visitors.
     *
     * @param conf apex configuration.
     * @param visitors list of visitors.
     * @return a scanner.
     */
    public static AstScanner<Grammar> create(ApexConfiguration conf, SquidAstVisitor<Grammar>... visitors) {
        final SourceProject sourceProject = new SourceProject("Apex Project");
        final SquidAstVisitorContextImpl<Grammar> context = new SquidAstVisitorContextImpl<>(sourceProject);
        final Parser<Grammar> parser = ApexParser.create(conf);

        AstScanner.Builder<Grammar> builder = AstScanner.<Grammar>builder(context).setBaseParser(parser);

        builder.withMetrics(ApexMetric.values());

        builder.setFilesMetric(ApexMetric.FILES);

        for (SquidAstVisitor<Grammar> visitor : visitors) {
            builder.withSquidAstVisitor(visitor);
        }

        return builder.build();
    }

    /**
     * Returns a source file from file and visitors.
     *
     * @param file source file.
     * @param visitors list of visitors.
     * @return a source.
     */
    public static SourceFile scanFile(File file, SquidAstVisitor<Grammar>... visitors) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("File '" + file + "' not found.");
        }
        AstScanner<Grammar> scanner = create(new ApexConfiguration(Charsets.UTF_8), visitors);
        scanner.scanFile(file);
        Collection<SourceCode> sources = scanner.getIndex().search(new QueryByType(SourceFile.class));
        if (sources.size() != 1) {
            throw new IllegalStateException("Only one SourceFile was expected whereas " + sources.size() + " has been returned.");
        }
        return (SourceFile) sources.iterator().next();
    }

}
