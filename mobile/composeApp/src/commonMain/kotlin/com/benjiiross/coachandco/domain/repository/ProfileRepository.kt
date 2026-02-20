package com.benjiiross.coachandco.domain.repository

import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.dto.auth.UserResponse
import com.benjiiross.coachandco.dto.profile.ProfileError
import com.benjiiross.coachandco.dto.profile.UpdateProfileRequest

interface ProfileRepository {
    suspend fun getProfile(): Outcome<UserResponse, ProfileError>
    suspend fun updateProfile(request: UpdateProfileRequest): Outcome<UserResponse, ProfileError>
}
