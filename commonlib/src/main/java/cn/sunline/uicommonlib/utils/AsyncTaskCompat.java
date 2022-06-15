package cn.sunline.uicommonlib.utils;

import android.os.AsyncTask;
import android.os.Build;

/**
 * <p>文件描述：<p>
 * <p>作者: DELL<p>
 * <p>创建时间：2018/12/4<p>
 */
public class AsyncTaskCompat {

    /**
     * Executes the task with the specified parameters, allowing multiple tasks to run in parallel
     * on a pool of threads managed by {@link android.os.AsyncTask}.
     *
     * @param task The {@link android.os.AsyncTask} to execute.
     * @param params The parameters of the task.
     * @return the instance of AsyncTask.
     */
    public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> executeParallel(
            AsyncTask<Params, Progress, Result> task,
            Params... params) {
        if (task == null) {
            throw new IllegalArgumentException("task can not be null");
        }
        //进行版本的判断，如果是3.0之前，则直接使用task.execute(params);
        //AsyncTaskCompatHoneycomb.executeParallel(task, params)实际上是显示调用了线程池THREAD_POOL_EXECUTOR
        if (Build.VERSION.SDK_INT >= 11) {
            // From API 11 onwards, we need to manually select the THREAD_POOL_EXECUTOR
            AsyncTaskCompatHoneycomb.executeParallel(task, params);
        } else {
            // Before API 11, all tasks were run in parallel
            task.execute(params);
        }

        return task;
    }

}

//AsyncTaskCompatHoneycomb源码代码
class AsyncTaskCompatHoneycomb {

    static <Params, Progress, Result> void executeParallel(
            AsyncTask<Params, Progress, Result> task,
            Params... params) {
        //这里显示调用了THREAD_POOL_EXECUTOR，所以就可以使用该线程池了
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

}
