/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Android Development_
    https://commonsware.com/Android
*/

package cs4720.studyhome;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Launchalot extends ListActivity {
  AppAdapter adapter=null;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    PackageManager pm=getPackageManager();
    Intent main=new Intent(Intent.ACTION_MAIN, null);
        
    main.addCategory(Intent.CATEGORY_LAUNCHER);

    List<ResolveInfo> launchables=pm.queryIntentActivities(main, 0);

    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
    Date d = new Date();
    String dayOfTheWeek = sdf.format(d);
    if (dayOfTheWeek.equals("Monday") || dayOfTheWeek.equals("Tuesday") || dayOfTheWeek.equals("Wednesday") ||
            dayOfTheWeek.equals("Thursday") || dayOfTheWeek.equals("Friday")) {
      for (Iterator<ResolveInfo> iter = launchables.listIterator(); iter.hasNext(); ) {
        ResolveInfo info = iter.next();
        if (info.loadLabel(pm).toString().equals("Facebook") ||
                info.loadLabel(pm).toString().equals("YouTube") ||
                info.loadLabel(pm).toString().equals("Snapchat") ||
                info.loadLabel(pm).toString().equals("Roll the Ball") ||
                info.loadLabel(pm).toString().equals("Play Games") ||
                info.loadLabel(pm).toString().equals("Chess Runner") ||
                info.loadLabel(pm).toString().equals("Angry Birds") ||
                info.loadLabel(pm).toString().equals("Temple Run") ||
                info.loadLabel(pm).toString().equals("Tumblr") ||
                info.loadLabel(pm).toString().equals("Twitter") ||
                info.loadLabel(pm).toString().equals("Play Movies & TV") ||
                info.loadLabel(pm).toString().equals("Miitomo") ||
                info.loadLabel(pm).toString().equals("Fruit Ninja Free") ||
                info.loadLabel(pm).toString().equals("Fruit Ninja") ||
                info.loadLabel(pm).toString().equals("Instagram") ||
                info.loadLabel(pm).toString().equals("Pinterest") ||
                info.loadLabel(pm).toString().equals("Google+") ||
                info.loadLabel(pm).toString().equals("Flow Free") ||
                info.loadLabel(pm).toString().equals("Glow Hockey") ||
                info.loadLabel(pm).toString().equals("Plague Inc.")) {
          iter.remove();
        }
      }
    }
    Collections.sort(launchables,
                     new ResolveInfo.DisplayNameComparator(pm));
    
    adapter=new AppAdapter(pm, launchables);
    setListAdapter(adapter);
  }
  
  @Override
  protected void onListItemClick(ListView l, View v,
                                 int position, long id) {
    ResolveInfo launchable=adapter.getItem(position);
    ActivityInfo activity=launchable.activityInfo;
    ComponentName name=new ComponentName(activity.applicationInfo.packageName,
                                         activity.name);
    Intent i=new Intent(Intent.ACTION_MAIN);
    
    i.addCategory(Intent.CATEGORY_LAUNCHER);
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
    i.setComponent(name);
    
    startActivity(i);    
  }
  
  class AppAdapter extends ArrayAdapter<ResolveInfo> {
    private PackageManager pm=null;
    
    AppAdapter(PackageManager pm, List<ResolveInfo> apps) {
      super(Launchalot.this, R.layout.row, apps);
      this.pm=pm;
    }
    
    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {
      if (convertView==null) {
        convertView=newView(parent);
      }
      
      bindView(position, convertView);
      
      return(convertView);
    }
    
    private View newView(ViewGroup parent) {
      return(getLayoutInflater().inflate(R.layout.row, parent, false));
    }
    
    private void bindView(int position, View row) {
      TextView label=(TextView)row.findViewById(R.id.label);
      
      label.setText(getItem(position).loadLabel(pm));
      
      ImageView icon=(ImageView)row.findViewById(R.id.icon);
      
      icon.setImageDrawable(getItem(position).loadIcon(pm));
    }
  }
}