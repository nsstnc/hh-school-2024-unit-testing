package ru.hh.school.unittesting.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LibraryManagerTest {

  private final UserService userService = new UserService() {
    @Override
    public boolean isUserActive(String userId) {
      return false;
    }
  };
  private final NotificationService notificationService = new NotificationService() {
    @Override
    public void notifyUser(String userId, String message) {

    }
  };
  private final LibraryManager libraryManager = new LibraryManager(notificationService, userService);


  @Test
  void testCalculateDynamicLateFeeWithoutBestSellerAndPremiumMember() {
    double dynamicLateFee = libraryManager.calculateDynamicLateFee(10, false, false);

    Assertions.assertEquals(5, dynamicLateFee);

  }


  @Test
  void testCalculateDynamicLateFeeWithBestSellerAndPremiumMember() {
    double dynamicLateFee = libraryManager.calculateDynamicLateFee(10, true, true);

    Assertions.assertEquals(6, dynamicLateFee);

  }

  @Test
  void calculateDynamicLateFeeShouldThrowException() {
    var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> libraryManager.calculateDynamicLateFee(-1, false, true));
    Assertions.assertEquals("Overdue days cannot be negative.", exception.getMessage());
  }


}
