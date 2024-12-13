package ru.hh.school.unittesting.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LibraryManagerTest {
  @Mock
  private UserService userService;
  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private LibraryManager libraryManager;


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
