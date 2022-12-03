package com.ptit.thuetruyenmgmt.entity;

import com.ptit.thuetruyenmgmt.model.FullName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;


@SpringBootTest
public class FullNameTest implements EntityTest {

    private FullName fullname;

    private final Integer expectedId = 1;

    private final String expectedFName = "an";

    private final String expectedLName = "trần văn";

    private Field id;
    private Field fName;
    private Field lName;


    @BeforeEach
    @Override
    public void initData() throws NoSuchFieldException {
        fullname = new FullName();
        id = fullname.getClass().getDeclaredField("id");
        fName = fullname.getClass().getDeclaredField("fName");
        lName = fullname.getClass().getDeclaredField("lName");
        id.setAccessible(true);
        fName.setAccessible(true);
        lName.setAccessible(true);
    }

    @Test
    @Override
    public void testFullArgConstructor() throws IllegalAccessException {
        fullname = new FullName(expectedId, expectedFName, expectedLName);

        assertEquals(expectedId, id.get(fullname));
        assertEquals(expectedFName, fName.get(fullname));
        assertEquals(expectedLName, lName.get(fullname));
    }

    @Test
    @Override
    public void testBuilder() throws IllegalAccessException {
        fullname = FullName.builder()
                .id(expectedId)
                .fName(expectedFName)
                .lName(expectedLName)
                .build();

        assertEquals(expectedId, id.get(fullname));
        assertEquals(expectedFName, fName.get(fullname));
        assertEquals(expectedLName, lName.get(fullname));
    }

    @Test
    @Override
    public void testSetters() throws IllegalAccessException {
        fullname.setId(expectedId);
        fullname.setFName(expectedFName);
        fullname.setLName(expectedLName);

        assertEquals(expectedId, id.get(fullname));
        assertEquals(expectedFName, fName.get(fullname));
        assertEquals(expectedLName, lName.get(fullname));
    }

    @Test
    @Override
    public void testGetters() throws IllegalAccessException {
        id.set(fullname, expectedId);
        fName.set(fullname, expectedFName);
        lName.set(fullname, expectedLName);

        assertEquals(expectedId, fullname.getId());
        assertEquals(expectedFName, fullname.getFName());
        assertEquals(expectedLName, fullname.getLName());
    }

    @Test
    @Override
    public void testToString() throws IllegalAccessException {
        String expectedString = "Trần Văn An";
        fName.set(fullname, expectedFName);
        lName.set(fullname, expectedLName);

        assertEquals(expectedString, fullname.toString());
    }

    @Test
    void testToString_whenNoFirstName() throws IllegalAccessException {
        // WHEN NULL
        String expectedString = "Trần Văn";
        lName.set(fullname, expectedLName);

        assertEquals(expectedString, fullname.toString());

        // WHEN EMPTY
        fName.set(fullname, " ");
        lName.set(fullname, expectedLName);
        assertEquals(expectedString, fullname.toString());
    }

    @Test
    void testToString_whenNoLastName() throws IllegalAccessException {
        // WHEN NULL
        String expectedString = "An";
        fName.set(fullname, expectedFName);

        assertEquals(expectedString, fullname.toString());

        // WHEN EMPTY
        fName.set(fullname, expectedFName);
        lName.set(fullname, " ");
        assertEquals(expectedString, fullname.toString());
    }

    @Override
    public void testEquals() {
    }

    @Override
    public void testHashCode() {

    }

}
