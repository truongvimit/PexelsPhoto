# Loading State Pattern - Clean Architecture

Đây là pattern chuẩn được sử dụng trong dự án để tạo smooth navigation và UX tốt như các app lớn (
Facebook, TikTok, Instagram).

## 🎯 Cấu trúc

### 1. Common LoadingState Enum

```kotlin
// Constants.kt
object ScreenState {
    enum class LoadingState {
        Loading,
        Success,
        Error
    }
}

// Type alias để code ngắn gọn
typealias LoadingState = Constants.ScreenState.LoadingState
```

### 2. Common Screen Components

```kotlin
// CommonScreenStates.kt - trong ui/components
@Composable
fun CommonLoadingScreen()

@Composable 
fun CommonErrorScreen()
```

### 3. Base Screen State Handler

```kotlin
// BaseScreenStateHandler.kt - trong presentation/base
@Composable
fun BaseScreenStateHandler(
    loadingState: LoadingState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    successContent: @Composable () -> Unit
)
```

## 🚀 Cách implement cho màn mới

### 1. ViewModel

```kotlin
class YourViewModel : BaseViewModel() {
    private val _stateFlow = MutableStateFlow(
        YourState(loadingState = LoadingState.Loading)
    )
    
    override fun onInit() {
        viewModelScope.launch {
            initializeData()
        }
    }
    
    private suspend fun initializeData() {
        try {
            // Delay for smooth navigation animation
            delay(300)
            
            // Load your data here
            loadData()
            
            _stateFlow.update {
                it.copy(
                    loadingState = LoadingState.Success,
                    data = result
                )
            }
        } catch (e: Exception) {
            _stateFlow.update {
                it.copy(
                    loadingState = LoadingState.Error,
                    error = e.message
                )
            }
        }
    }
    
    fun retry() {
        viewModelScope.launch {
            _stateFlow.update {
                it.copy(loadingState = LoadingState.Loading, error = null)
            }
            initializeData()
        }
    }
}

data class YourState(
    val loadingState: LoadingState = LoadingState.Loading,
    val error: String? = null,
    val data: YourData? = null
)
```

### 2. Screen Composable

```kotlin
@Composable
fun YourScreen(navController: NavHostController) {
    val viewModel: YourViewModel = koinViewModel()
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    BaseScreenStateHandler(
        loadingState = uiState.loadingState,
        onBackClick = navController::navigateUp,
        onRetry = viewModel::retry,
        loadingText = "Loading your data...",
        errorTitle = "Failed to load your data",
        error = uiState.error
    ) {
        YourSuccessContent(
            data = uiState.data,
            onAction = { /* handle actions */ }
        )
    }
}

@Composable
private fun YourSuccessContent(
    data: YourData?,
    onAction: () -> Unit
) {
    // Your actual screen content here
}
```

## ✅ Lợi ích

1. **Smooth Navigation**: Loading screen hiện ngay, animation không bị lag
2. **Consistent UX**: Tất cả màn hình có cùng loading/error behavior
3. **DRY Principle**: Không duplicate code
4. **Easy Maintenance**: Chỉnh sửa một chỗ, tất cả màn áp dụng
5. **Industry Standard**: Pattern được dùng bởi các app lớn

## 🎨 Customization

### Custom Loading Text

```kotlin
BaseScreenStateHandler(
    loadingText = "Loading products...",
    errorTitle = "Failed to load products"
) { /* content */ }
```

### Hide Back Button

```kotlin
BaseScreenStateHandler(
    showBackButton = false
) { /* content */ }
```

### Custom Error Handling

```kotlin
BaseScreenStateHandler(
    error = uiState.error,
    onRetry = { 
        viewModel.retry()
        analytics.track("retry_clicked")
    }
) { /* content */ }
```

## 📱 Examples

- ✅ SearchScreen - ĐÃ IMPLEMENT
- ✅ HomeScreen - ĐÃ IMPLEMENT

## 🔗 Related Files

- `Constants.kt` - LoadingState enum & alias
- `ui/components/CommonScreenStates.kt` - Common loading/error components
- `presentation/base/BaseScreenStateHandler.kt` - Main handler
- `SearchScreen.kt` - Implementation examples