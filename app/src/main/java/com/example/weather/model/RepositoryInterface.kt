package com.example.weather.model

import com.example.weather.database.LocalDataSource
import com.example.weather.network.RemoteSource

interface RepositoryInterface :LocalDataSource,RemoteSource{
}