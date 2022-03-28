package studio.archetype.hologui2.utils.file;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ReloadableFolder {

    private final File dir;
    private final boolean recursive;
    private final Map<File, Long> children;
    private final List<ReloadableFolder> childrenFolders;
    private final List<File> created, changed, deleted;
    private final ChangesRunnable deletedRunnable, changedRunnable, createdRunnable;

    public ReloadableFolder(File file, boolean recursive, ChangesRunnable deleted, ChangesRunnable changed, ChangesRunnable created) {
        if(!file.exists())
            file.mkdirs();
        this.dir = file;
        this.recursive = recursive;
        this.children = Maps.newHashMap();
        this.childrenFolders = Lists.newArrayList();
        this.created = Lists.newArrayList();
        this.changed = Lists.newArrayList();
        this.deleted = Lists.newArrayList();

        this.deletedRunnable = deleted;
        this.changedRunnable = changed;
        this.createdRunnable = created;

        populateChildren();
    }

    public void detectChanges() {
        created.clear();
        changed.clear();
        deleted.clear();
        Map<File, Long> oldStatus = Map.copyOf(children);

        populateChildren();

        oldStatus.keySet().forEach(f -> {
            if(!children.containsKey(f))
                deleted.add(f);
        });

        children.forEach((k, v) -> {
            if(!oldStatus.containsKey(k))
                created.add(k);
            else {
                if(v.equals(oldStatus.get(k)))
                    changed.add(k);
            }
        });

        if(!created.isEmpty())
            createdRunnable.onChange(created);
        if(!changed.isEmpty())
            changedRunnable.onChange(changed);
        if(!deleted.isEmpty())
            deletedRunnable.onChange(deleted);

        if(recursive) {
            List<ReloadableFolder> oldFolders = List.copyOf(childrenFolders);
            oldFolders.forEach(f -> {
                if(!oldFolders.contains(f))
                    deleted.addAll(f.getAllChildren());
            });
            childrenFolders.forEach(ReloadableFolder::detectChanges);
        }
    }

    public void checkChangeOnly() {
        changed.clear();
        Map<File, Long> oldStatus = Map.copyOf(children);
        children.forEach((k, v) -> {
            if(oldStatus.containsKey(k) && k.exists())
                if(v.equals(oldStatus.get(k)))
                    changed.add(k);
        });

        if(!changed.isEmpty())
            changedRunnable.onChange(changed);

        if(recursive) {
            List<ReloadableFolder> oldFolders = List.copyOf(childrenFolders);
            childrenFolders.forEach(f -> {
                if(oldFolders.contains(f) && f.dir.exists())
                    f.checkChangeOnly();
            });
        }

    }

    private void populateChildren() {
        for(File f : dir.listFiles()) {
            if(f.isDirectory()) {
                if(recursive && childrenFolders.stream().noneMatch(folder -> f.equals(folder.dir)))
                    childrenFolders.add(new ReloadableFolder(f, true, deletedRunnable, changedRunnable, createdRunnable));
            } else {
                if(!children.containsKey(f))
                    children.put(f, f.lastModified());
            }
        }

        children.keySet().forEach(f -> {
            if(!f.exists())
                children.remove(f);
        });


        if(recursive)
            childrenFolders.forEach(f -> {
                if(!f.dir.exists())
                    childrenFolders.remove(f);
            });
    }

    public List<File> getAllChildren() {
        List<File> all = Lists.newArrayList(children.keySet());
        if(recursive)
            childrenFolders.forEach(f -> all.addAll(f.getAllChildren()));
        return all;
    }

    public interface ChangesRunnable {
        void onChange(List<File> files);
    }
}
