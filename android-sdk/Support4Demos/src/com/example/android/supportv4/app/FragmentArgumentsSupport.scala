/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.supportv4.app

import com.example.android.supportv4.R

import android.support.v4.app.{FragmentActivity, Fragment, FragmentTransaction}

import android.app.Activity
import android.content.res.TypedArray
import android.os.Bundle
import android.util.AttributeSet
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.TextView

/**
 * Demonstrates a fragment that can be configured through both Bundle arguments
 * and layout attributes.
 */
class FragmentArgumentsSupport extends FragmentActivity {
  import FragmentArgumentsSupport._  // companion object

  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_arguments_support)

    if (savedInstanceState == null) {
      // First-time init; create fragment to embed in activity.
      val ft = getSupportFragmentManager.beginTransaction()
      val newFragment = MyFragment.newInstance("From Arguments")
      ft.add(R.id.created, newFragment)
      ft.commit()
    }
  }
}

object FragmentArgumentsSupport{

  private object MyFragment {
    /**
     * Create a new instance of MyFragment that will be initialized
     * with the given arguments.
     */
    def newInstance(label: CharSequence): MyFragment = {
      val f = new MyFragment()
      val b = new Bundle()
      b.putCharSequence("label", label)
      f setArguments b
      f
    }
  }

  private class MyFragment extends Fragment {
    private var mLabel: CharSequence = _

    /**
     * Parse attributes during inflation from a view hierarchy into the
     * arguments we handle.
     */
    override def onInflate(activity: Activity, attrs: AttributeSet,
                           savedInstanceState: Bundle) {
      super.onInflate(activity, attrs, savedInstanceState)

      val a = activity.obtainStyledAttributes(attrs,
                    R.styleable.FragmentArguments)
      mLabel = a.getText(R.styleable.FragmentArguments_android_label)
      a.recycle()
    }

    /**
     * During creation, if arguments have been supplied to the fragment
     * then parse those out.
     */
    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)

      val args = getArguments
      if (args != null) {
        val label = args getCharSequence "label"
        if (label != null) mLabel = label
      }
    }

    /**
     * Create the view for this fragment, using the arguments given to it.
     */
    override def onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle): View = {
      val v = inflater.inflate(R.layout.hello_world, container, false)
      val tv = v.findViewById(R.id.text).asInstanceOf[TextView]
      tv setText (if (mLabel != null) mLabel else "(no label)")
      tv setBackgroundDrawable getResources.getDrawable(android.R.drawable.gallery_thumb)
      v
    }
  }

}
