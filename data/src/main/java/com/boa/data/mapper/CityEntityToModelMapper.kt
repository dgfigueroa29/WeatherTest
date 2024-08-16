package com.boa.data.mapper

import com.boa.data.entity.CityEntity
import com.boa.domain.base.BaseMapper
import com.boa.domain.model.CityModel

class CityEntityToModelMapper : BaseMapper<CityEntity, CityModel>() {
    override fun map(input: CityEntity): CityModel = CityModel(
        input.id,
        input.name,
        input.state ?: "",
        input.country,
        input.latitude,
        input.longitude,
        input.selected == true
    )
}