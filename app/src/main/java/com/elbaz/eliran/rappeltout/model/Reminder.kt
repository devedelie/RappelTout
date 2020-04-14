package com.elbaz.eliran.rappeltout.model

import java.util.*

/**
 * Created by Eliran Elbaz on 13-Apr-20.
 */
data class Reminder (var title : String, var content : String, var eventColor : Int, var alert : Date, var isActive : Boolean )