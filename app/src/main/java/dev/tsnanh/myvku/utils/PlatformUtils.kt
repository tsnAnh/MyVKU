@file:Suppress("NOTHING_TO_INLINE")

package dev.tsnanh.myvku.utils

import android.os.Build

inline fun isQPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
inline fun isMPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M