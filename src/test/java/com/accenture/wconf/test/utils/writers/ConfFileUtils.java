package com.accenture.wconf.test.utils.writers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfFileUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfFileUtils.class);
	
	public static void writeFileBeforeTest(Path path, List<String> contents) {
		deleteTestResource(path);
		writeContentsAsText(path, contents);
	}
	
	public static void deleteTestResource(Path path) {
		LOGGER.debug("About to delete {} (if exists)", path);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			LOGGER.error("Could not delete file {}", path, e);
			throw new IllegalStateException(e);
		}		
	}
	
	public static void copyFileBeforeTest(Path srcFilePath, Path tgtFilePath) {
		try {
			Files.copy(srcFilePath, tgtFilePath);
		} catch (IOException e) {
			LOGGER.error("Could not perform copy operation {} => {}", srcFilePath, tgtFilePath, e);
		}
	}
	
	private static void writeContentsAsText(Path path, List<String> contents) {
		LOGGER.debug("About to write {} with the given contents", path);
		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
			PrintWriter printWriter = new PrintWriter(bufferedWriter);
			contents.stream().forEach(line -> printWriter.println(line));
			bufferedWriter.close();
			LOGGER.debug("Successfully written {} with {} line{}.", path, contents.size(), contents.size() == 1? "" : "s");
		} catch (IOException e) {
			LOGGER.error("Coult not write file for the test execution: {}", path, e);
			throw new IllegalStateException(e);
		}		
		
		if (!Files.exists(path)) {
			throw new IllegalStateException("Somehow, created file was not accessible");
		}
	}	
}
