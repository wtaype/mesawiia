package com.mesawii.feature.bienvenida

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mesawii.core.kicss.FzSmart
import com.mesawii.core.kicss.WiCss
import com.mesawii.core.kicss.WiIcons
import com.mesawii.core.kicss.WiText
import com.mesawii.core.kidev.GlassCard
import com.mesawii.core.kidev.GoldPill
import com.mesawii.core.kidev.WiButton
import com.mesawii.core.kidev.wiStore
import kotlinx.coroutines.launch

data class OnboardingSlide(
    val titulo: String,
    val descripcion: String,
    val icono: ImageVector,
    val tag: String
)

/**
 * 🦉 BienvenidaScreen.kt — Onboarding estilo Duolingo de 3 slides interactivos glassmorphism.
 * Muestra el recorrido por primera vez al instalar la app.
 */
@Composable
fun BienvenidaScreen(
    onComenzar: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val store = remember { wiStore(context) }

    val slides = listOf(
        OnboardingSlide(
            titulo = "¡Bienvenido a MesaWii!",
            descripcion = "La suite moderna y ultra veloz para el control total de tu cafetería y restaurante.",
            icono = WiIcons.Restaurant,
            tag = "TECNOLOGÍA 2026"
        ),
        OnboardingSlide(
            titulo = "Salón & Comandas en Vivo",
            descripcion = "Controla el estado de tus mesas en tiempo real y envía pedidos directo al barista sin demoras.",
            icono = WiIcons.PointOfSale,
            tag = "TIEMPO REAL"
        ),
        OnboardingSlide(
            titulo = "Caja & Flujo de Cobro Infalible",
            descripcion = "Cobros por tarjeta, Yape, Plin o efectivo con arqueos a ciegas y cero faltantes.",
            icono = WiIcons.BarChart,
            tag = "CONTROL FINANCIERO"
        )
    )

    val pagerState = rememberPagerState(pageCount = { slides.size })

    val completarOnboarding = {
        store.saveBool("is_first_launch", false)
        onComenzar()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        WiCss.bg,
                        WiCss.wb,
                        WiCss.bg
                    )
                )
            )
            .padding(20.dp)
    ) {
        // Botón "Saltar" en esquina superior derecha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GoldPill("MesaWii POS")

            Text(
                text = "Saltar ➔",
                style = WiText.small,
                color = WiCss.mco,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { completarOnboarding() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }

        // Pager Central de 3 Slides
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) { page ->
            val slide = slides[page]
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                intensity = 0.35f,
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(WiCss.mco.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = slide.icono,
                            contentDescription = null,
                            tint = WiCss.mco,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    GoldPill(slide.tag)

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = slide.titulo,
                        style = WiText.h1,
                        color = WiCss.tx,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = slide.descripcion,
                        style = WiText.body,
                        color = WiCss.tx2,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Footer: Indicadores de puntos + Botón Siguiente / Comenzar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Indicadores Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(slides.size) { iteration ->
                    val isSelected = pagerState.currentPage == iteration
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(if (isSelected) 24.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) WiCss.mco else WiCss.tx3.copy(alpha = 0.4f))
                    )
                }
            }

            // Botón de avance
            val esUltimo = pagerState.currentPage == slides.size - 1
            WiButton(
                text = if (esUltimo) "🚀 Comenzar Ahora" else "Siguiente ➔",
                onClick = {
                    if (esUltimo) {
                        completarOnboarding()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
