package com.example.disastergigihapp.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disastergigihapp.R
import com.example.disastergigihapp.domain.repository.ApiRepository
import com.example.disastergigihapp.util.ApiState
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.scalebar.scalebar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(private val apiRepository: ApiRepository) : ViewModel() {

    private val postStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)

    val postStateFlows: StateFlow<ApiState> = postStateFlow

    fun loadMap(mapView: MapView) {
        //initialize map view, set the style, and hide the scale bar
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        mapView.scalebar.enabled = false
    }

    fun getRecent(timeperiod: Int) = viewModelScope.launch {
        postStateFlow.value = ApiState.Loading
        apiRepository.getRecent(timeperiod)
            .catch { e ->
                postStateFlow.value = ApiState.Failure(e)
            }.collect { data ->
                postStateFlow.value = ApiState.Success(data)
            }
    }

    fun getRecentAdmin(timeperiod: Int, admin: String) = viewModelScope.launch {
        postStateFlow.value = ApiState.Loading
        apiRepository.getRecentAdmin(timeperiod, admin)
            .catch { e ->
                postStateFlow.value = ApiState.Failure(e)
            }.collect { data ->
                postStateFlow.value = ApiState.Success(data)
            }
    }

    fun getRecentDisaster(timeperiod: Int, disaster: String) = viewModelScope.launch {
        postStateFlow.value = ApiState.Loading
        apiRepository.getRecentDisaster(timeperiod, disaster)
            .catch { e ->
                postStateFlow.value = ApiState.Failure(e)
            }.collect { data ->
                postStateFlow.value = ApiState.Success(data)
            }
    }

    fun getRecentAll(timeperiod: Int, admin: String, disaster: String) =
        viewModelScope.launch {
            postStateFlow.value = ApiState.Loading
            apiRepository.getRecentAll(timeperiod, admin, disaster)
                .catch { e ->
                    postStateFlow.value = ApiState.Failure(e)
                }.collect { data ->
                    postStateFlow.value = ApiState.Success(data)
                }
        }

    fun addAnnotationToMap(Lng: Double, Lat: Double, context: Context, mapView: MapView) {
        //documentation by mapbox
        // Create an instance of the Annotation API and get the PointAnnotationManager.
        bitmapFromDrawableRes(
            context,
            R.drawable.baseline_place_24
        )?.let {
            val annotationApi = mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()
            // Set options for the resulting symbol layer.
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                // Define a geographic coordinate.
                .withPoint(Point.fromLngLat(Lng, Lat))
                // Specify the bitmap you assigned to the point annotation
                // The bitmap will be added to map style automatically.
                .withIconImage(it)
            // Add the resulting pointAnnotation to the map.
            pointAnnotationManager.create(pointAnnotationOptions)
        }
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            // copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
}