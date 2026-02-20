package com.benjiiross.coachandco.fakes

import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.domain.repository.ProfileRepository
import com.benjiiross.coachandco.dto.auth.UserResponse
import com.benjiiross.coachandco.dto.profile.ProfileError
import com.benjiiross.coachandco.dto.profile.UpdateProfileRequest

class FakeProfileRepository : ProfileRepository {
    var getProfileResult: Outcome<UserResponse, ProfileError> = Outcome.Failure(ProfileError.NotFound)
    var updateProfileResult: Outcome<UserResponse, ProfileError> = Outcome.Failure(ProfileError.NotFound)

    override suspend fun getProfile(): Outcome<UserResponse, ProfileError> = getProfileResult
    override suspend fun updateProfile(request: UpdateProfileRequest): Outcome<UserResponse, ProfileError> = updateProfileResult
}
