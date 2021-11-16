package de.dagere.importJsonCreator;

import org.junit.Assert;
import org.junit.Test;

import de.dagere.issueImporter.data.Util;

public class TestReplacement {
   @Test
   public void testTabReplacement() {
      String original = "This\t is a test";
      String replaced = Util.cleanBody(original);
      Assert.assertEquals("This    is a test", replaced);
   }
   
   @Test
   public void testQutationReplacement() {
      String original = "This \"is\" a test";
      String replaced = Util.cleanBody(original);
      Assert.assertEquals("This 'is' a test", replaced);
   }
}
