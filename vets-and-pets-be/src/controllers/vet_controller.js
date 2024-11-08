import VetService from "../services/vet_service.js";

class VetController {
    async addVet(req, res) {
        try {
            const vet = req.body;
            const newVet = await VetService.addVet(vet);
            res.status(200).json(newVet);
        } catch (error) {
            console.log(error);
            res.status(500).json(error);
        }
    }

    async getVets(req, res) {
        try {
            const vets = await VetService.getVets();
            res.status(200).json(vets);
        } catch (error) {
            console.log(error);
            res.status(500).json(error);
        }
    }

    async getVetById(req, res) {
        try {
            const id = req.params.id;
            const vet = await VetService.getVetById(id);
            res.status(200).json(vet);
        } catch (error) {
            res.status(500).json(error);
        }
    }

    async deleteAllVets(req, res) {
        try {
            await VetService.deleteAllVets();
            res.status(200).json('All vets deleted');
        } catch (error) {
            res.status(500).json(error);
        }
    }
}

export default new VetController();