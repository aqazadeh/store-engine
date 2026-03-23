package az.kon.academy.aggragate.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessStatusTest {

    @Test
    void predicateMethods() {
        assertTrue(ProcessStatus.IN_PROGRESS.isInProgress());
        assertFalse(ProcessStatus.IN_PROGRESS.isFailed());
        assertFalse(ProcessStatus.IN_PROGRESS.isCompleted());
        assertFalse(ProcessStatus.IN_PROGRESS.isRollBacked());

        assertTrue(ProcessStatus.FAILED.isFailed());
        assertTrue(ProcessStatus.COMPLETED.isCompleted());
        assertTrue(ProcessStatus.ROLL_BACKED.isRollBacked());
    }
}

