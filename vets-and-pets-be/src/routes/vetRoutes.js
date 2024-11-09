import vet_controller from '../controllers/vet_controller.js';

/**
 * @swagger
 * /api/vets:
 *   get:
 *     summary: Get all vets
 *     responses:
 *       200:
 *         description: A list of vets
 */
export const getVets = (req, res) => {
  vet_controller.getVets(req, res);
};

/**
 * @swagger
 * /api/vets/{id}:
 *   get:
 *     summary: Get a vet by ID
 *     parameters:
 *       - name: id
 *         in: path
 *         required: true
 *         description: The vet's ID
 *     responses:
 *       200:
 *         description: A single vet object
 *       404:
 *         description: Vet not found
 */
export const getVetById = (req, res) => {
  vet_controller.getVetById(req, res);
};

/**
 * @swagger
 * /api/vets:
 *   post:
 *     summary: Add a new vet
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               name:
 *                 type: string
 *               description:
 *                 type: string
 *               location:
 *                 type: object
 *                 properties:
 *                   longitude:
 *                     type: number
 *                     example: -122.4194
 *                   latitude:
 *                     type: number
 *                     example: 37.7749
 *     responses:
 *       201:
 *         description: Vet added successfully
 */
export const addVet = (req, res) => {
  vet_controller.addVet(req, res);
};

/**
 * @swagger
 * /api/vets:
 *   delete:
 *     summary: Delete all vets
 *     responses:
 *       200:
 *         description: All vets deleted
 */
export const deleteAllVets = (req, res) => {
  vet_controller.deleteAllVets(req, res);
};
