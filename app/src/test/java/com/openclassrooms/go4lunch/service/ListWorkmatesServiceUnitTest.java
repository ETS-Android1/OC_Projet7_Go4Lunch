package com.openclassrooms.go4lunch.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.openclassrooms.go4lunch.model.Workmate;
import com.openclassrooms.go4lunch.service.workmates.ListWorkmatesService;
import com.openclassrooms.go4lunch.service.workmates.ServiceWorkmatesCallback;
import com.openclassrooms.go4lunch.utils.AppInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * File providing tests to cover methods
 * from @{@link com.openclassrooms.go4lunch.service.workmates.ListWorkmatesService} class file.
 */
@RunWith(JUnit4.class)
public class ListWorkmatesServiceUnitTest {

    public ListWorkmatesService service;
    @Mock
    public Context context;
    @Mock
    SharedPreferences sharedPreferences;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(context.getSharedPreferences(AppInfo.FILE_FIRESTORE_USER_ID, Context.MODE_PRIVATE))
                .thenReturn(sharedPreferences);
        Mockito.when(sharedPreferences.getString(AppInfo.PREF_FIRESTORE_USER_ID_KEY, null))
                .thenReturn(AppInfo.PREF_FIRESTORE_USER_ID_KEY);
    }

    @Test
    public void test() {
        ServiceWorkmatesCallback callback = new ServiceWorkmatesCallback() {
            @Override
            public void onWorkmatesAvailable(List<Workmate> listWorkmates) {
                assertFalse(listWorkmates.isEmpty());
            }
        };

        service = new ListWorkmatesService(context);
        service.getEmployeesInfoFromFirestoreDatabase(callback);
    }

}
