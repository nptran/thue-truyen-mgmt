package com.ptit.thuetruyenmgmt.model;

import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PenaltyTest implements EntityTest {

    private Penalty penalty;

    private final Integer expectedId = 1;
    private final String expectedName = "Lỗi 1";
    private final String expectedDescription = "Mô tả lỗi";
    private final double expectedRecommendedFee = 1000.99;
    private List<RentedBookPenalty> expectedDetectedPenalties;

    private Field id;
    private Field name;
    private Field description;
    private Field recommendedFee;
    private Field detectedPenalties;


    @BeforeEach
    @Override
    public void initData() throws NoSuchFieldException {
        penalty = new Penalty();

        RentedBookPenalty rbp1 = RentedBookPenalty.builder().id(new RentedBookPenaltyKey(100, expectedId)).fee(1000).build();
        RentedBookPenalty rbp2 = RentedBookPenalty.builder().id(new RentedBookPenaltyKey(101, expectedId)).fee(1000).build();
        RentedBookPenalty rbp3 = RentedBookPenalty.builder().id(new RentedBookPenaltyKey(102, expectedId)).fee(1000).build();
        expectedDetectedPenalties = new ArrayList<>();
        expectedDetectedPenalties.add(rbp1);
        expectedDetectedPenalties.add(rbp2);
        expectedDetectedPenalties.add(rbp3);

        id = penalty.getClass().getDeclaredField("id");
        name = penalty.getClass().getDeclaredField("name");
        description = penalty.getClass().getDeclaredField("description");
        recommendedFee = penalty.getClass().getDeclaredField("recommendedFee");
        detectedPenalties = penalty.getClass().getDeclaredField("detectedPenalties");
        id.setAccessible(true);
        name.setAccessible(true);
        description.setAccessible(true);
        recommendedFee.setAccessible(true);
        detectedPenalties.setAccessible(true);
    }

    @Test
    @Override
    public void testFullArgConstructor() throws IllegalAccessException {
        penalty = new Penalty(
                expectedId,
                expectedName,
                expectedDescription,
                expectedRecommendedFee,
                expectedDetectedPenalties
        );

        assertFields(false);
    }

    @Test
    @Override
    public void testBuilder() throws IllegalAccessException {
        penalty = Penalty.builder()
                .id(expectedId)
                .name(expectedName)
                .description(expectedDescription)
                .recommendedFee(expectedRecommendedFee)
                .detectedPenalties(expectedDetectedPenalties)
                .build();

        assertFields(false);
    }

    @Test
    @Override
    public void testSetters() throws IllegalAccessException {
        penalty.setId(expectedId);
        penalty.setName(expectedName);
        penalty.setDescription(expectedDescription);
        penalty.setRecommendedFee(expectedRecommendedFee);
        penalty.setDetectedPenalties(expectedDetectedPenalties);

        assertFields(false);
    }

    @Test
    @Override
    public void testGetters() throws IllegalAccessException {
        id.set(penalty, expectedId);
        name.set(penalty, expectedName);
        description.set(penalty, expectedDescription);
        recommendedFee.set(penalty, expectedRecommendedFee);
        detectedPenalties.set(penalty, expectedDetectedPenalties);

        assertFields(true);
    }

    @Override
    public void testToString() throws IllegalAccessException {
    }

    @Test
    @Override
    public void testEquals() throws NoSuchFieldException, IllegalAccessException {
        // When its id is NULL
        Penalty anotherPenalty = new Penalty();
        Field anotherId = anotherPenalty.getClass().getDeclaredField("id");
        anotherId.setAccessible(true);
        anotherId.set(anotherPenalty, expectedId);
        assertFalse(penalty.equals(anotherPenalty));

        id.set(penalty, expectedId);

        // Compare to itself
        Penalty clonePenalty = penalty;
        assertTrue(penalty.equals(clonePenalty));

        // Compare to NULL
        assertFalse(penalty.equals(null));

        // Compare to another same id
        anotherId.set(anotherPenalty, expectedId);
        assertTrue(penalty.equals(anotherPenalty));

        // Compare to another different id
        anotherId.set(anotherPenalty, 404);
        assertFalse(penalty.equals(anotherPenalty));

        // Compare to another different id = null
        anotherId.set(anotherPenalty, null);
        assertFalse(penalty.equals(anotherPenalty));

        // Compare to another Type
        Object obj = new Object();
        assertFalse(penalty.equals(obj));
    }

    @Test
    @Override
    public void testHashCode() {
        int expectedNumber = Penalty.class.hashCode();
        assertEquals(expectedNumber, penalty.hashCode());
    }

    private void assertFields(boolean useGetter) throws IllegalAccessException {
        // THEN
        if (!useGetter) {
            assertEquals(expectedId, id.get(penalty));
            assertEquals(expectedName, name.get(penalty));
            assertEquals(expectedDescription, description.get(penalty));
            assertEquals(expectedRecommendedFee, recommendedFee.get(penalty));
            assertEquals(expectedDetectedPenalties, detectedPenalties.get(penalty));
            return;
        }

        assertEquals(expectedId, penalty.getId());
        assertEquals(expectedName, penalty.getName());
        assertEquals(expectedDescription, penalty.getDescription());
        assertEquals(expectedRecommendedFee, penalty.getRecommendedFee());
        assertEquals(expectedDetectedPenalties, penalty.getDetectedPenalties());
    }

}
