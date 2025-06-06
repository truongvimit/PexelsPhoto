package com.example.tv.pexels.core

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Tiện ích chuyên biệt để ẩn riêng thanh điều hướng (navigation bar) mà vẫn giữ nguyên thanh trạng thái
 * (status bar) trên mọi phiên bản Android với khả năng phòng vệ cao trước các null pointer exception.
 */
object NavigationBarUtils {
    
    /**
     * Ẩn thanh điều hướng (navigation bar) nhưng vẫn giữ nguyên thanh trạng thái (status bar)
     *
     * @param activity Activity đang sử dụng
     * @param isImmersiveSticky true cho chế độ IMMERSIVE_STICKY, false cho chế độ IMMERSIVE thông thường
     */
    @JvmOverloads
    fun hideNavigationBar(activity: Activity, isImmersiveSticky: Boolean = true) {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                // Sử dụng WindowInsetsControllerCompat - API hiện đại và ổn định nhất từ AndroidX
                WindowCompat.setDecorFitsSystemWindows(activity.window, false)
                val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)

                // Chỉ ẩn thanh điều hướng, giữ nguyên thanh trạng thái
                controller.hide(WindowInsetsCompat.Type.navigationBars())

                // Thiết lập behavior phù hợp
                controller.systemBarsBehavior = if (isImmersiveSticky) {
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                } else {
                    WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                }
            }else{
                // Fallback an toàn với cách tiếp cận từ API cũ
                applyLegacyNavigationHide(activity, isImmersiveSticky)
            }
        } catch (e: Exception) {
            // Fallback an toàn với cách tiếp cận từ API cũ
            applyLegacyNavigationHide(activity, isImmersiveSticky)
        }
    }
    
    /**
     * Phương pháp dự phòng sử dụng system UI flags được chứng minh là ổn định
     * trên đa dạng thiết bị và phiên bản Android
     */
    @Suppress("DEPRECATION")
    private fun applyLegacyNavigationHide(activity: Activity, isImmersiveSticky: Boolean) {
        activity.window.decorView.post {
            try {
                // Flag cơ bản để ẩn chỉ thanh điều hướng
                var flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

                // Thêm flag immersive nếu có hỗ trợ
                flags = if (isImmersiveSticky) {
                    flags or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                } else {
                    flags or View.SYSTEM_UI_FLAG_IMMERSIVE
                }

                activity.window.decorView.systemUiVisibility = flags
            } catch (e: Exception) {
                // Bảo vệ cuối cùng trong trường hợp xảy ra lỗi không mong muốn
            }
        }
    }
    
    /**
     * Hiện lại thanh điều hướng khi cần thiết
     * @param activity Activity đích
     */
    fun showNavigationBar(activity: Activity) {
        try {
            // Sử dụng API hiện đại
            val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
            controller.show(WindowInsetsCompat.Type.navigationBars())
            
            // Khôi phục trạng thái decorView nếu cần
            WindowCompat.setDecorFitsSystemWindows(activity.window, true)
        } catch (e: Exception) {
            // Fallback an toàn
            try {
                @Suppress("DEPRECATION")
                activity.window.decorView.post {
                    // Khôi phục trạng thái mặc định của UI
                    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                }
            } catch (e: Exception) {
                // Không làm gì
            }
        }
    }
}