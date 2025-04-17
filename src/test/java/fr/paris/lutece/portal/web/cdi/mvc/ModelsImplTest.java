package fr.paris.lutece.portal.web.cdi.mvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the ModelsImpl class.
 *
 * @author Manfred Riem (manfred.riem at oracle.com)
 */
public class ModelsImplTest {

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        ModelsImpl models = new ModelsImpl();
        assertNull(models.get("K"));
        models.put("K", "V");
        assertNotNull(models.get("K"));
        assertEquals("V", models.get("K"));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        ModelsImpl models = new ModelsImpl();
        assertNotNull(models.iterator());
    }
}
