package codes.kooper.quarryTools.database.services;

import codes.kooper.koopKore.database.managers.AbstractDataManager;
import codes.kooper.quarryTools.database.models.PickaxeStorage;
import codes.kooper.shaded.mongodb.client.model.Filters;

public class PickaxeService extends AbstractDataManager<PickaxeStorage> {

    public PickaxeService() {
        super("pickaxes", PickaxeStorage.class);
    }

    /**
     * Save a pick storage to the database.
     *
     * @param pickaxeStorage The Pick Storage to save
     */
    public void savePickStorage(PickaxeStorage pickaxeStorage) {
        findById(pickaxeStorage.getId()).ifPresentOrElse((u) -> collection.replaceOne(Filters.eq("_id", pickaxeStorage.getId()), pickaxeStorage), () -> collection.insertOne(pickaxeStorage));
    }
}
