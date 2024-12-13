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

  @Test
  void testAddBook(){
    libraryManager.addBook("1", 2);
    libraryManager.addBook("2", 3);
    libraryManager.addBook("1", 4);
    libraryManager.addBook("2", 1);

    Assertions.assertAll(
        () -> Assertions.assertEquals(6, libraryManager.getAvailableCopies("1")),
        () -> Assertions.assertEquals(4, libraryManager.getAvailableCopies("2"))
    );
  }

  
}
