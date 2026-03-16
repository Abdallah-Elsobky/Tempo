package iti.student.finalproject.presentation.screen.favorites.map.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import iti.student.finalproject.R
import iti.student.finalproject.utils.DrawableHelper
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun StreetMapView(
    initialLat: Double,
    initialLon: Double,
    onLocationSelected: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    AndroidView(
        factory = {
            mapView.apply {
                setMultiTouchControls(true)

                val controller = controller
                controller.setZoom(14.0)
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                val startPoint = GeoPoint(initialLat, initialLon)
                controller.setCenter(startPoint)

                val marker = Marker(this)
                marker.position = startPoint
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = "Selected Location"
                marker.icon = DrawableHelper.resizeDrawable(
                    context,
                    R.drawable.ic_location,
                    100,
                    100,
                    Color.Red.toArgb()
                )

                overlays.add(marker)

                val mapEventsReceiver = object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(point: GeoPoint?): Boolean {
                        point?.let {
                            marker.position = it

                            val projection = mapView.projection
                            val point = projection.toPixels(it, null)
                            mapView.controller.zoomInFixing(point.x, point.y)
                            invalidate()
                            onLocationSelected(it.latitude, it.longitude)
                        }
                        return true
                    }

                    override fun longPressHelper(p: GeoPoint?): Boolean {

                        p?.let {

                            val projection = mapView.projection
                            val point = projection.toPixels(it, null)

                            mapView.controller.zoomInFixing(point.x, point.y)

                        }

                        return true
                    }
                }

                overlays.add(org.osmdroid.views.overlay.MapEventsOverlay(mapEventsReceiver))
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { view ->
            val newPoint = GeoPoint(initialLat, initialLon)
            view.controller.setCenter(newPoint)

            val marker = view.overlays.filterIsInstance<Marker>().firstOrNull()
            if (marker != null) {
                marker.position = newPoint
            }

            view.invalidate()
        }
    )
}