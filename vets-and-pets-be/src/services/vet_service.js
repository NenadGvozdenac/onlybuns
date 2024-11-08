import Vet from "../domain/mongoose_models/vet.js";

class VetService {
    addVet(vet) {
        return Vet.create(vet);
    }

    getVets() {
        return Vet.find();
    }

    getVetById(id) {
        return Vet.findById(id);
    }

    deleteAllVets() {
        return Vet.deleteMany();
    }
}

export default new VetService();