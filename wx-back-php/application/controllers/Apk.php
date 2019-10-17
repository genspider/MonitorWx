<?php

use Apk\Apk;

/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2019/8/30
 * Time: 14:10
 */

class ApkController extends Controller
{
    function updateAction()
    {
        $parmas = $this->getRequest()->getQuery();
        $apk = new Apk();
        $res = $apk->update($parmas);
        \Response\OutputHelper::outPut($res);
    }

    function insertAction()
    {
        $parmas = $this->getRequest()->getQuery();
        $apk = new Apk();
        $res = $apk->insert($parmas);
        \Response\OutputHelper::outPut($res);
    }

    function listAction()
    {
        $parmas = $this->getRequest()->getQuery();
        $apk = new Apk();
        $res = $apk->list($parmas);
        \Response\OutputHelper::outPut($res);
    }

    function findoneAction()
    {
        $parmas = $this->getRequest()->getQuery();
        $apk = new Apk();
        $res = $apk->getByName($parmas);
        \Response\OutputHelper::outPut($res);
    }
}