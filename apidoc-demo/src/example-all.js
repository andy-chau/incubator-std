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
 * @api {get} /url 报文头规范
 * @apiExample Example usage:
 *     curl -i http://localhost/
 * @apiName requestBody
 * @apiGroup ALL
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
 * @api {get} /url 响应码枚举
 * @apiExample Example usage:
 *     curl -i http://localhost/
 * @apiName responseBody
 * @apiGroup ALL
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