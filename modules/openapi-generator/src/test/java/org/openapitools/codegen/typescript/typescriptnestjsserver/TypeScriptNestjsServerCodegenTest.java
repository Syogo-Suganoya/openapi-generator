package org.openapitools.codegen.typescript.typescriptnestjsserver;

import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.TestUtils;
import org.openapitools.codegen.config.CodegenConfigurator;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TypeScriptNestjsServerCodegenTest {

    private Path generate(String fileNaming) throws Exception {
        final File output = Files.createTempDirectory("typescriptnestjsserver_").toFile();
        output.deleteOnExit();

        final CodegenConfigurator configurator = new CodegenConfigurator()
                .setGeneratorName("typescript-nestjs-server")
                .setInputSpec("src/test/resources/3_0/typescript-nestjs-server/oneof-inline-model-import.yaml")
                .addAdditionalProperty("fileNaming", fileNaming)
                .setOutputDir(output.getAbsolutePath().replace("\\", "/"));

        final ClientOptInput clientOptInput = configurator.toClientOptInput();
        final List<File> files = new DefaultGenerator().opts(clientOptInput).generate();
        files.forEach(File::deleteOnExit);
        return output.toPath();
    }

    @Test
    public void testModelImportsMatchGeneratedFileNamesWithKebabCase() throws Exception {
        Path output = generate("kebab-case");

        TestUtils.assertFileExists(output.resolve("models/overlap-one-of.ts"));
        TestUtils.assertFileExists(output.resolve("models/overlap-one-of-1.ts"));
        TestUtils.assertFileContains(output.resolve("models/overlap.ts"),
                "import { OverlapOneOf } from './overlap-one-of';",
                "import { OverlapOneOf1 } from './overlap-one-of-1';");
        TestUtils.assertFileNotContains(output.resolve("models/overlap.ts"), "'./overlap-one-of1'");
    }

    @Test
    public void testModelImportsMatchGeneratedFileNamesWithCamelCase() throws Exception {
        Path output = generate("camelCase");

        TestUtils.assertFileExists(output.resolve("models/overlapOneOf.ts"));
        TestUtils.assertFileExists(output.resolve("models/overlapOneOf1.ts"));
        TestUtils.assertFileContains(output.resolve("models/overlap.ts"),
                "import { OverlapOneOf } from './overlapOneOf';",
                "import { OverlapOneOf1 } from './overlapOneOf1';");
    }
}
