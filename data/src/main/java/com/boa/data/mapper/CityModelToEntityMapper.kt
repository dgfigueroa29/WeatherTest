package com.boa.data.mapper

import com.boa.data.entity.CityEntity
import com.boa.data.entity.Coord
import com.boa.domain.base.BaseMapper
import com.boa.domain.model.CityModel

class CityModelToEntityMapper : BaseMapper<CityModel, CityEntity>() {
    override fun map(input: CityModel): CityEntity = CityEntity(
        input.id,
        input.name,
        input.state,
        input.country,
        Coord(
            input.lat,
            input.lon
        ),
        input.selected
    )
}