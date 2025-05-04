# Koin Dependency Injection Reference Guide

## Setup

1. **Add dependencies**:
   ```kotlin
  implementation 'io.insert-koin:koin-android:3.5.0'
  implementation 'io.insert-koin:koin-androidx-compose:3.5.0'
   ```

2. **Create module definitions**:
   ```kotlin
   val appModule = module {
       single { DatabaseRepository(get()) }
       factory { AnalyticsViewModel() }
       viewModel { ProfileViewModel(get()) }
   }
   ```

3. **Initialize in Application class**:
   ```kotlin
   class MyApp : Application() {
       override fun onCreate() {
           super.onCreate()
           startKoin {
               androidContext(this@MyApp)
               modules(appModule)
           }
       }
   }
   ```

## Injection Methods

### 1. Property Delegation (`by inject()`)
```kotlin
class YourClass : KoinComponent {
    private val repository: INotificationRepository by inject()
}
```
**Use case**: For regular classes outside Android components. Suitable for service classes, utilities, and repositories.

### 2. Component-Specific Injection
```kotlin
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
}
```
**Use case**: For injecting ViewModels or dependencies directly in Android components.

### 3. Scope-Based Injection
```kotlin
class MainActivity : AppCompatActivity(), AndroidScopeComponent {
    override val scope: Scope by activityScope()
    private val repository: Repository by inject()
}
```
**Use case**: When dependencies should live within a specific scope (activity/fragment lifecycle).

### 4. Compose Injection (`getViewModel()`)
```kotlin
@Composable
fun YourScreen() {
    val viewModel: YourViewModel = getViewModel()
}
```
**Use case**: In Compose UI for ViewModels that should be scoped to the composable's lifecycle owner.

### 5. Parameter Injection
```kotlin
@Composable
fun DetailScreen(id: String) {
    val viewModel: DetailViewModel = getViewModel { parametersOf(id) }
}
```
**Use case**: When dependencies need runtime parameters.

## Key Differences

- `getViewModel()`: Used in Composables, lifecycle-aware, and automatically scoped to the composition's lifecycle owner
- `by inject()`: Used with property delegation in regular classes, lazily resolves dependencies based on Koin module definitions