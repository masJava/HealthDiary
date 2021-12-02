package mas.com.health_diary.ui.main

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_health.view.*
import mas.com.health_diary.R
import mas.com.health_diary.data.entity.Health


class MainAdapter(val onItemClick: ((Health) -> Unit)? = null) :
    RecyclerView.Adapter<MainAdapter.HealthViewHolder>() {

    var healthList: List<Pair<String, List<Health>>> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HealthViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_health,
                parent,
                false
            )
        )

    override fun getItemCount() = healthList.size

    override fun onBindViewHolder(vh: HealthViewHolder, pos: Int) = vh.bind(healthList[pos])

    inner class HealthViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(healthByDay: Pair<String, List<Health>>) = with(itemView) {
            dateTextView.text = healthByDay.first

            tableLayout.removeAllViews()
            val sortedList = healthByDay.second.sortedBy {
                it.dateFull
            }
            val gravityCenter = Gravity.CENTER
            for (health in sortedList) {
                val row = TableRow(context).apply {
                    gravity = gravityCenter
                    setOnClickListener { onItemClick?.invoke(health) }
                }
                val time_tv = TextView(context).apply {
                    text = health.time
                    gravity = gravityCenter
                }
                val max_pressure_tv = TextView(context).apply {
                    text = health.max.toString()
                    gravity = gravityCenter
                    setTextColor(resources.getColor(R.color.text_black, null))
                    textSize = 19f
                }
                val splitter_pressure_tv = TextView(context).apply {
                    text = "/"
                    gravity = gravityCenter
                }
                val min_pressure_tv = TextView(context).apply {
                    text = health.min.toString()
                    gravity = gravityCenter
                    setTextColor(resources.getColor(R.color.text_black, null))
                    textSize = 19f
                }
                val heart_iv =
                    ImageView(context).apply {
                        setImageResource(R.drawable.ic_baseline_favorite_24)
                        layoutParams = TableRow.LayoutParams(40, 40)
                    }
                val heart_tv = TextView(context).apply {
                    text = health.pulse.toString()
                    gravity = gravityCenter
                }
                row.addView(time_tv)
                row.addView(max_pressure_tv)
                row.addView(splitter_pressure_tv)
                row.addView(min_pressure_tv)
                row.addView(heart_iv)
                row.addView(heart_tv)
                row.background = setBackground(health, resources)
                tableLayout.addView(row)
            }
        }

        private fun setBackground(health: Health, res: Resources): Drawable? {
            var background: Drawable? = null
            with(health) {
                if (max >= 180 && min >= 120) {
                    background =
                        ResourcesCompat.getDrawable(res, R.drawable.gradient_dark_red, null)
                } else
                    if (max >= 140 && min >= 90) {
                        background = ResourcesCompat.getDrawable(res, R.drawable.gradient_red, null)
                    } else
                        if (max >= 130 && min >= 80) {
                            background =
                                ResourcesCompat.getDrawable(res, R.drawable.gradient_orange, null)
                        } else
                            if (max >= 120 && min < 80) {
                                background = ResourcesCompat.getDrawable(
                                    res,
                                    R.drawable.gradient_yellow,
                                    null
                                )
                            } else
                                if (max < 120 && min < 80) {
                                    background = ResourcesCompat.getDrawable(
                                        res,
                                        R.drawable.gradient_green,
                                        null
                                    )
                                }
            }
            return background
        }
    }

}
