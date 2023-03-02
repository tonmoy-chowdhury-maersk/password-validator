# password-validator


A password validator utility that supports the following operations:-

- Validations:-
  - password should be larger than 8 chars
  - password should not be null
  - password should have one uppercase letter at least
  - password should have one lowercase letter at least
  - password should have one number at least
- Password is OK if at least three of the previous conditions is true
- Password is never OK if item it does not include at least one lowercase letter.
- Ability to run the above validations in parallel to improve overall performance.
