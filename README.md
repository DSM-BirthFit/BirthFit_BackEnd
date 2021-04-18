# BirthFit_BackEnd
## Folder Structure
```
📦 BirthFit_BackEnd
├─ .github
│  └─ workflows
│     └─ CI.yml
├─ .gitignore
├─ README.md
├─ build.gradle.kts
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
├─ settings.gradle.kts
└─ src
   ├─ main
   │  ├─ kotlin
   │  │  └─ com
   │  │     └─ birth
   │  │        └─ fit
   │  │           ├─ FitApplication.kt
   │  │           ├─ config
   │  │           │  ├─ BeanConfig.kt
   │  │           │  ├─ CorsConfig.kt
   │  │           │  ├─ RedisConfig.kt
   │  │           │  └─ SwaggerConfig.kt
   │  │           ├─ controller
   │  │           │  ├─ EmailController.kt
   │  │           │  ├─ HelpController.kt
   │  │           │  ├─ QnaController.kt
   │  │           │  └─ UserController.kt
   │  │           ├─ domain
   │  │           │  ├─ entity
   │  │           │  │  ├─ Email.kt
   │  │           │  │  ├─ Help.kt
   │  │           │  │  ├─ HelpComment.kt
   │  │           │  │  ├─ HelpLike.kt
   │  │           │  │  ├─ HelpLikePK.kt
   │  │           │  │  ├─ Qna.kt
   │  │           │  │  ├─ QnaAnswer.kt
   │  │           │  │  ├─ QnaLike.kt
   │  │           │  │  ├─ QnaLikePK.kt
   │  │           │  │  └─ User.kt
   │  │           │  ├─ enums
   │  │           │  │  └─ EmailVerificationStatus.kt
   │  │           │  └─ repository
   │  │           │     ├─ EmailRepository.kt
   │  │           │     ├─ HelpCommentRepository.kt
   │  │           │     ├─ HelpLikeRepository.kt
   │  │           │     ├─ HelpRepository.kt
   │  │           │     ├─ QnaAnswerRepository.kt
   │  │           │     ├─ QnaLikeRepository.kt
   │  │           │     ├─ QnaRepository.kt
   │  │           │     └─ UserRepository.kt
   │  │           ├─ dto
   │  │           │  ├─ ChangePasswordRequest.kt
   │  │           │  ├─ ChangeProfileRequest.kt
   │  │           │  ├─ ContentRequest.kt
   │  │           │  ├─ EmailVerifyRequest.kt
   │  │           │  ├─ HelpCommentResponse.kt
   │  │           │  ├─ HelpContentResponse.kt
   │  │           │  ├─ HelpListResponse.kt
   │  │           │  ├─ HelpPageResponse.kt
   │  │           │  ├─ JoinRequest.kt
   │  │           │  ├─ LoginRequest.kt
   │  │           │  ├─ PostRequest.kt
   │  │           │  ├─ ProfileResponse.kt
   │  │           │  ├─ QnaAnswerResponse.kt
   │  │           │  ├─ QnaContentResponse.kt
   │  │           │  ├─ QnaListResponse.kt
   │  │           │  ├─ QnaPageResponse.kt
   │  │           │  └─ TokenResponse.kt
   │  │           ├─ exception
   │  │           │  ├─ ApiExceptionHandler.kt
   │  │           │  ├─ ErrorResponse.kt
   │  │           │  └─ error
   │  │           │     ├─ ContentNotFoundException.kt
   │  │           │     ├─ ExpiredTokenException.kt
   │  │           │     ├─ InvalidAuthCodeException.kt
   │  │           │     ├─ InvalidAuthEmailException.kt
   │  │           │     ├─ InvalidTokenException.kt
   │  │           │     ├─ LoginFailedException.kt
   │  │           │     ├─ PasswordSameException.kt
   │  │           │     ├─ PostNotFoundException.kt
   │  │           │     ├─ UserAlreadyExistException.kt
   │  │           │     └─ UserNotFoundException.kt
   │  │           ├─ service
   │  │           │  ├─ EmailService.kt
   │  │           │  ├─ HelpService.kt
   │  │           │  ├─ QnaService.kt
   │  │           │  └─ UserService.kt
   │  │           └─ util
   │  │              ├─ AES256Util.kt
   │  │              └─ JwtTokenProvider.kt
   │  └─ resources
   │     └─ application.yml
   └─ test
      └─ kotlin
         └─ com
            └─ birth
               └─ fit
                  └─ FitApplicationTests.kt
```