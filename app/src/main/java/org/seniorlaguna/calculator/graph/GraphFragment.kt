package org.seniorlaguna.calculator.example

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.udojava.evalex.UserDefinedFunction
import kotlinx.android.synthetic.main.fragment_graph.*
import org.seniorlaguna.calculator.Function
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import java.math.BigDecimal
import kotlin.Exception

class GraphFragment : Fragment() {

    companion object {
        const val TOOL_ID = 5
    }

    // view models
    protected lateinit var globalViewModel: GlobalViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // get view models
        globalViewModel =  ViewModelProviders.of(requireActivity())[GlobalViewModel::class.java]

        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadScreenConfig()

        // get all functions from database and convert them to UserDefinedFunctions
        globalViewModel.database.getAllFunctionsLive().observe(requireActivity(), Observer<List<Function>> { functions ->

            graph_viewer.functions.clear()

            functions.forEach {f ->
                graph_viewer.functions.add(
                    UserDefinedFunction(f.expression)
                )
            }

            graph_viewer.redraw()

        })
    }

    override fun onResume() {
        super.onResume()
        loadScreenConfig()
    }

    private fun loadScreenConfig() {
        try {
            graph_viewer.apply {
                xMin = globalViewModel.settings.graph.minX.toDouble()
                xMax = globalViewModel.settings.graph.maxX.toDouble()
                yMin = globalViewModel.settings.graph.minY.toDouble()
                yMax = globalViewModel.settings.graph.maxY.toDouble()

                if (xMin >= xMax) throw Exception()
                if (yMin >= yMax) throw Exception()

                redraw()
            }
        } catch (e : Exception) {}
    }

}


class GraphView(context : Context, attr : AttributeSet? = null) : View(context, attr, 0) {

    val functionPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth  = 6f
    }

    val axisPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 4f
    }

    // functions to be drawn
    val functions = ArrayList<UserDefinedFunction>()

    // visible area
    var xMin : Double = -10.0
    var xMax : Double = 10.0
    var yMin : Double = -10.0
    var yMax : Double = 10.0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawAxis(canvas!!)

        // nothing to do
        if (functions.isEmpty()) return

        functions.forEach {
            drawFunction(it, canvas!!)
        }

    }

    private fun drawAxis(canvas : Canvas) {
        if ((xMin < 0) and (0 < xMax)) {
            val y = height - ((-yMin) / (yMax - yMin) * height)
            canvas.drawLine(0f, y.toFloat(), width.toFloat(), y.toFloat(), axisPaint)
        }

        if ((yMin < 0) and (0 < yMax)) {
            val x = (-xMin) / (xMax - xMin) * width
            canvas.drawLine(x.toFloat(), 0f, x.toFloat(), height.toFloat(), axisPaint)
        }
    }


    private fun drawFunction(function: UserDefinedFunction, canvas : Canvas) {
        val ys = calculateValues(function)

        for (i in 0 until width) {
            ys[i]?.let {
                canvas.drawPoint(i.toFloat(), it.toFloat(), functionPaint)
            }
        }
    }

    private fun calculateValues(function : UserDefinedFunction) : Array<Double?> {

        val result = Array<Double?>(width, fun(i) = null)
        val xStepSize = (xMax - xMin) / (width - 1)

        for (i in 0 until width) {
            try {
                result[i] = function.eval(mapOf("x" to BigDecimal(xMin + (i * xStepSize))))
                result[i] = height - ((result[i]!! - yMin) / (yMax - yMin) * height)

            } catch (e : Exception) {}
        }

        return result
    }

    fun redraw() {
        invalidate()
    }
}