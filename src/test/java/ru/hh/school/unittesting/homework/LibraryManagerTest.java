package ru.hh.school.unittesting.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibraryManagerTest {
  @Mock
  private UserService userService;
  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private LibraryManager libraryManager;

  @ParameterizedTest
  @CsvSource({
      "10, false, false",
  })
  void testCalculateDynamicLateFeeWithoutBestSellerAndPremiumMember(
      Integer overdueDays,
      Boolean isBestseller,
      Boolean isPremiumMember
  ) {
    double dynamicLateFee = libraryManager.calculateDynamicLateFee(overdueDays, isBestseller, isPremiumMember);

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

  @Test
  void testBorrowBookWithInActiveUser(){
    when(userService.isUserActive("1")).thenReturn(false);
    boolean result = libraryManager.borrowBook("1", "1");
    Assertions.assertFalse(result);
  }

  @Test
  void testBorrowBookWithActiveUser(){
    when(userService.isUserActive("1")).thenReturn(true);
    libraryManager.addBook("1", 2);
    boolean result = libraryManager.borrowBook("1", "1");
    Assertions.assertTrue(result);
  }

  @Test
  void testBorrowBookWithoutAvailableCopies(){
    when(userService.isUserActive("1")).thenReturn(true);
    boolean result = libraryManager.borrowBook("1", "1");
    Assertions.assertFalse(result);
  }

  @Test
  void testReturnBookWithoutBorrowedBook(){
    boolean result = libraryManager.returnBook("1", "1");
    Assertions.assertFalse(result);
  }

  @Test
  void testReturnBookWithoutBorrowedBookByUser(){
    when(userService.isUserActive("1")).thenReturn(true);
    libraryManager.addBook("1", 2);
    libraryManager.borrowBook("1", "1");

    boolean result = libraryManager.returnBook("1", "2");
    Assertions.assertFalse(result);
  }

  @Test
  void testReturnBook(){
    when(userService.isUserActive("1")).thenReturn(true);
    libraryManager.addBook("1", 2);
    libraryManager.borrowBook("1", "1");

    boolean result = libraryManager.returnBook("1", "1");
    Assertions.assertTrue(result);
  }

}
