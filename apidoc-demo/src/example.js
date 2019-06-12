/**
 * @apiDefine UserNotFoundError
 *
 * @apiError UserNotFound The id of the User was not found.
 *
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 404 Not Found
 *     {
 *       "error": "UserNotFound"
 *     }
 */

/**
 * @api {get} /user/:id 01-Request User info
 * @apiExample {curl} Example usage:
 *     curl -i http://localhost/user/4711
 * @apiName GetUser
 * @apiGroup User
 * @apiVersion 1.0.0
 * @apiPermission none
 *
 * @apiParam {Number} id Users unique ID.
 *
 * @apiParamExample {json} Request-Example:
 *     {
 *       "id": 4711
 *     }
 *
 * @apiSuccess {String} firstname Firstname of the User.
 * @apiSuccess {String} lastname  Lastname of the User.
 *
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *       "firstname": "John",
 *       "lastname": "Doe"
 *     }
 *
 * @apiUse UserNotFoundError
 */

/**
 * @api {put} /user/ 02-Modify User info
 * @apiExample {curl} Example usage:
 *     curl -i http://localhost/user/
 * @apiName PutUser
 * @apiGroup User
 * @apiVersion 1.0.0
 *
 * @apiParam {Number} id          Users unique ID.
 * @apiParam {String} [firstname] Firstname of the User.
 * @apiParam {String} [lastname]  Lastname of the User.
 *
 * @apiParamExample {json} Request-Example:
 *     {
 *       "id": 4711,
 *       "firstname": "张",
 *       "lastname": "三",
 *     }
 *
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *
 * @apiUse UserNotFoundError
 */