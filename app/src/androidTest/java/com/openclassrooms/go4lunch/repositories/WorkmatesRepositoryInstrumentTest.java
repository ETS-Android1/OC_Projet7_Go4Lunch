package com.openclassrooms.go4lunch.repositories;

import android.content.Context;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import com.google.firebase.firestore.DocumentReference;
import com.openclassrooms.go4lunch.service.workmates.ServiceWorkmatesCallback;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * File providing tests to cover methods from @{@link WorkmatesRepository} class file.
 */
public class WorkmatesRepositoryInstrumentTest {

    @Rule
    public final ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    public WorkmatesRepository workmatesRepository;


    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        workmatesRepository = new WorkmatesRepository(context);
    }

    /**
     * TEST #1 : test method getEmployeesInfoFromFirestoreDatabase()
     * Checks if list of workmates is correctly retrieved from NoSQL Cloud Firestore
     * database.
     */
    @Test
    public void test_check_if_list_of_workmates_is_correctly_retrieved_from_database() {
        ServiceWorkmatesCallback callback = listWorkmates -> assertFalse(listWorkmates.isEmpty());
        workmatesRepository.getEmployeesInfoFromFirestoreDatabase(callback);
    }

    /**
     * TEST #2 : test method getDocumentReferenceCurrentUser()
     * Checks if a DocumentReference object is correctly retrieved from database, using its ID.
     */
    @Test
    public void test_check_if_document_reference_is_correctly_retrieved_from_database() {
        // ID of an existing Workmate in database
        String DOCUMENT_CURRENT_USER_ID = "3fAFONRzI2mMUyVOcWfS";
        DocumentReference documentReference = workmatesRepository.getDocumentReferenceCurrentUser(DOCUMENT_CURRENT_USER_ID);
        assertNotNull(documentReference);
    }
}
