package org.openapitools.codegen.cppoatpp;

import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.TestUtils;
import org.openapitools.codegen.config.CodegenConfigurator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class CppOatppCodegenTest {

    @DataProvider(name = "generators")
    public Object[][] generators() {
        return new Object[][]{{"cpp-oatpp-client"}, {"cpp-oatpp-server"}};
    }

    @Test(dataProvider = "generators")
    public void testEnumValueFunctionNamesAreValidIdentifiers(String generatorName) throws Exception {
        File output = Files.createTempDirectory("cppoatpp_").toFile();
        output.deleteOnExit();

        final CodegenConfigurator configurator = new CodegenConfigurator()
                .setGeneratorName(generatorName)
                .setInputSpec("src/test/resources/3_0/cpp-oatpp-client/enum-value-names.yaml")
                .setOutputDir(output.getAbsolutePath().replace("\\", "/"));
        new DefaultGenerator().opts(configurator.toClientOptInput()).generate().forEach(File::deleteOnExit);

        Path mode = output.toPath().resolve("model/Mode.hpp");
        // function names are valid C++ identifiers, the returned values are the original enum values
        TestUtils.assertFileContains(mode,
                "static Mode fast_mode(void) { return \"fast-mode\"; }",
                "static Mode slow_mode(void) { return \"slow-mode\"; }",
                "static Mode available(void) { return \"available\"; }",
                "static Mode r_1st(void) { return \"1st\"; }",
                "static Mode r_delete(void) { return \"delete\"; }",
                "static Mode with_space(void) { return \"with space\"; }");
        TestUtils.assertFileNotContains(mode, "fast-mode(void)");
    }
}
