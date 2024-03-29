package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.utils.model.request.ReplaceFileVA;
import com.finalproject.automated.refactoring.tool.utils.service.ReplaceFileHelper;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 17 June 2019
 */

@Service
public class ReplaceFileHelperImpl implements ReplaceFileHelper {

    private static final String NEW_LINE_DELIMITER = "\n";

    @Override
    public synchronized Boolean replaceFile(@NonNull ReplaceFileVA replaceFileVA) {
        Path filePath = Paths.get(replaceFileVA.getFilePath());
        return doReplaceFile(filePath, replaceFileVA);
    }

    private Boolean doReplaceFile(Path filePath, ReplaceFileVA replaceFileVA) {
        try {
            String fileContent = getFileContent(filePath);
            String newFileContent = createNewFileContent(fileContent, replaceFileVA);

            replaceContent(filePath, newFileContent);
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private String getFileContent(Path filePath) throws IOException {
        return Files.lines(filePath)
                .collect(Collectors.joining(NEW_LINE_DELIMITER));
    }

    private String createNewFileContent(String fileContent,
                                        ReplaceFileVA replaceFileVA) throws Exception {
        String quoteReplacement = Matcher.quoteReplacement(replaceFileVA.getReplacement());
        Matcher matcher = Pattern.compile(replaceFileVA.getTarget())
                .matcher(fileContent);

        if (matcher.find()) {
            return matcher.replaceAll(quoteReplacement);
        } else {
            throw new Exception();
        }
    }

    private void replaceContent(Path filePath, String newFileContent) throws IOException {
        Files.write(filePath, newFileContent.getBytes());
    }
}
