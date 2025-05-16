package com.example.keychainapp.logic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KeychainServiceTest {

    private KeychainService keychainService;

    @Before
    public void setUp() {
        keychainService = new KeychainService();
    }

    @Test
    public void testSaveAndRetrieveKeyValue() {
        String key = "testKey";
        String value = "testValue";

        keychainService.save(key, value);
        String retrievedValue = keychainService.retrieve(key);

        assertEquals(value, retrievedValue);
    }

    @Test
    public void testRetrieveNonExistentKey() {
        String retrievedValue = keychainService.retrieve("nonExistentKey");

        assertNull(retrievedValue);
    }

    @Test
    public void testOverwriteKeyValue() {
        String key = "overwriteKey";
        String initialValue = "initialValue";
        String newValue = "newValue";

        keychainService.save(key, initialValue);
        keychainService.save(key, newValue);
        String retrievedValue = keychainService.retrieve(key);

        assertEquals(newValue, retrievedValue);
    }

    @Test
    public void testSaveNullKey() {
        String value = "value";

        try {
            keychainService.save(null, value);
            fail("Expected IllegalArgumentException for null key");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testSaveNullValue() {
        String key = "key";

        try {
            keychainService.save(key, null);
            fail("Expected IllegalArgumentException for null value");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}