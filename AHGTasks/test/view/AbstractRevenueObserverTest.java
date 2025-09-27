package test.view;

import model.Amount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.AbstractRevenueObserver;

import static org.junit.jupiter.api.Assertions.*;

class AbstractRevenueObserverTest {

    private TestObserver testObserver;

    @BeforeEach
    void setUp() {
        testObserver = new TestObserver();
    }

    @Test
    void testNewRevenue_updatesLastRevenue() {
        Amount input = new Amount(100.0);
        testObserver.newRevenue(input);
        assertEquals(input.getAmount(), testObserver.getLastRevenue().getAmount(), "Last revenue should match the input.");
    }

    @Test
    void testNewRevenue_accumulatesTotalRevenue() {
        testObserver.newRevenue(new Amount(40.0));
        testObserver.newRevenue(new Amount(60.0));
        assertEquals(100.0, testObserver.getTotalRevenue().getAmount(), 0.001, "Total revenue should be accumulated correctly.");
    }

    @Test
    void testNewRevenue_callsDoShowTotalIncome() {
        testObserver.newRevenue(new Amount(20.0));
        assertTrue(testObserver.wasDoShowTotalIncomeCalled(), "doShowTotalIncome() should be called.");
    }

    @Test
    void testNewRevenue_handlesExceptionGracefully() {
        testObserver.setShouldThrow(true);
        testObserver.newRevenue(new Amount(10.0));
        assertTrue(testObserver.wasHandleErrorsCalled(), "handleErrors() should be triggered when an exception occurs.");
    }

    private static class TestObserver extends AbstractRevenueObserver {
        private boolean shouldThrow = false;
        private boolean doShowCalled = false;
        private boolean errorHandled = false;

        @Override
        protected void doShowTotalIncome() throws Exception {
            doShowCalled = true;
            if (shouldThrow) {
                throw new Exception("Simulated error");
            }
        }

        @Override
        protected void handleErrors(Exception e) {
            errorHandled = true;
        }

        public void setShouldThrow(boolean value) {
            this.shouldThrow = value;
        }

        public boolean wasDoShowTotalIncomeCalled() {
            return doShowCalled;
        }

        public boolean wasHandleErrorsCalled() {
            return errorHandled;
        }

        public Amount getLastRevenue() {
            return lastRevenue;
        }

        public Amount getTotalRevenue() {
            return totalRevenue;
        }
    }
}