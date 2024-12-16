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
      "10, false, false, 5",
      "10, true, true, 6",
      "10, true, false, 7.5",
      "10, false, true, 4",
      "0, false, true, 0",
  })
  void testCalculateDynamicLateFee(
      Integer overdueDays,
      Boolean isBestseller,
      Boolean isPremiumMember,
      double expectedDynamicLateFee
  ) {
    double dynamicLateFee = libraryManager.calculateDynamicLateFee(overdueDays, isBestseller, isPremiumMember);

    Assertions.assertEquals(expectedDynamicLateFee, dynamicLateFee);

  }


  @Test
  void calculateDynamicLateFeeShouldThrowException() {
    var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> libraryManager.calculateDynamicLateFee(-1, false, true));
    Assertions.assertEquals("Overdue days cannot be negative.", exception.getMessage());
  }

  @Test
  void testAddBook() {
    libraryManager.addBook("1", 2);
    libraryManager.addBook("1", 4);

    Assertions.assertEquals(6, libraryManager.getAvailableCopies("1"));
  }

  @Test
  void testBorrowBookWithInActiveUser() {
    when(userService.isUserActive("1")).thenReturn(false);
    boolean result = libraryManager.borrowBook("1", "1");
    Assertions.assertFalse(result);
  }

  @Test
  void testBorrowBookWithActiveUser() {
    when(userService.isUserActive("1")).thenReturn(true);
    libraryManager.addBook("1", 1);
    boolean result = libraryManager.borrowBook("1", "1");
    Assertions.assertTrue(result);
    Assertions.assertEquals(0, libraryManager.getAvailableCopies("1"));
  }

  @Test
  void testBorrowBookWithoutAvailableCopies() {
    when(userService.isUserActive("1")).thenReturn(true);
    boolean result = libraryManager.borrowBook("1", "1");
    Assertions.assertFalse(result);
    Assertions.assertEquals(0, libraryManager.getAvailableCopies("1"));
  }

  @Test
  void testReturnBookWithoutBorrowedBook() {
    boolean result = libraryManager.returnBook("1", "1");
    Assertions.assertFalse(result);
  }

  @Test
  void testReturnBookWithoutBorrowedBookByUser() {
    when(userService.isUserActive("1")).thenReturn(true);
    libraryManager.addBook("1", 1);
    libraryManager.borrowBook("1", "1");

    boolean result = libraryManager.returnBook("1", "2");
    Assertions.assertFalse(result);
    Assertions.assertEquals(0, libraryManager.getAvailableCopies("1"));

  }

  @Test
  void testReturnBook() {
    when(userService.isUserActive("1")).thenReturn(true);
    libraryManager.addBook("1", 1);
    libraryManager.borrowBook("1", "1");

    boolean result = libraryManager.returnBook("1", "1");
    Assertions.assertTrue(result);
    Assertions.assertEquals(1, libraryManager.getAvailableCopies("1"));
  }

}
