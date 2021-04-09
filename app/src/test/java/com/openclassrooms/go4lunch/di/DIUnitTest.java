package com.openclassrooms.go4lunch.di;

import android.content.Context;
import com.openclassrooms.go4lunch.database.Go4LunchDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import java.util.concurrent.Executor;
import retrofit2.Retrofit;
import static org.junit.Assert.assertNotNull;

/**
 * File providing tests to cover methods from @{@link DI} class file.
 */
@RunWith(JUnit4.class)
public class DIUnitTest {

    /**
     * TEST #1 : Checks if the DI class provides a singleton instance of @{@link Go4LunchDatabase}.
     */
    @Test
    public void test_di_database_provider() {
        Context mockContext = Mockito.mock(Context.class);
        Go4LunchDatabase database = DI.provideDatabase(mockContext);
        assertNotNull(database);
    }

    /**
     * TEST #2 : Checks if the DI class provides an Executor object instance.
     */
    @Test
    public void test_di_executor_provider() {
        Executor executor = DI.provideExecutor();
        assertNotNull(executor);
    }

    /**
     * TEST #3 : Checks if the DI class provides a Retrofit instance.
     */
    @Test
    public void test_di_retrofit_provider() {
        Retrofit retrofit = DI.provideRetrofit();
        assertNotNull(retrofit);
    }

}