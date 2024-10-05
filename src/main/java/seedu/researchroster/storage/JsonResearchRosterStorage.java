package seedu.researchroster.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.researchroster.commons.core.LogsCenter;
import seedu.researchroster.commons.exceptions.DataLoadingException;
import seedu.researchroster.commons.exceptions.IllegalValueException;
import seedu.researchroster.commons.util.FileUtil;
import seedu.researchroster.commons.util.JsonUtil;
import seedu.researchroster.model.ReadOnlyResearchRoster;

/**
 * A class to access ResearchRoster data stored as a json file on the hard disk.
 */
public class JsonResearchRosterStorage implements ResearchRosterStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonResearchRosterStorage.class);

    private Path filePath;

    public JsonResearchRosterStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getAddressBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyResearchRoster> readAddressBook() throws DataLoadingException {
        return readAddressBook(filePath);
    }

    /**
     * Similar to {@link #readAddressBook()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyResearchRoster> readAddressBook(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableResearchRoster> jsonAddressBook = JsonUtil.readJsonFile(
                filePath, JsonSerializableResearchRoster.class);
        if (!jsonAddressBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonAddressBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveAddressBook(ReadOnlyResearchRoster addressBook) throws IOException {
        saveAddressBook(addressBook, filePath);
    }

    /**
     * Similar to {@link #saveAddressBook(ReadOnlyResearchRoster)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveAddressBook(ReadOnlyResearchRoster addressBook, Path filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableResearchRoster(addressBook), filePath);
    }

}
