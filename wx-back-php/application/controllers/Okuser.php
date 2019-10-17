<?php
/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2019/8/14
 * Time: 16:50
 */

class OkuserController extends Controller
{
    public function loginAction()
    {
        $parmas = $this->getRequest()->getQuery();
        $user = new \OkUser\OkUser();
        $res = $user->login($parmas);
        \Response\OutputHelper::outPut($res);
    }

    public function addFirendAction()
    {
        $parmas = $this->getRequest()->getQuery();
        $user = new \OkUser\OkUser();
        $res = $user->addFirend($parmas);
        \Response\OutputHelper::outPut($res);
    }

    public function firendListAction()
    {
        $parmas = $this->getRequest()->getQuery();
        $user = new \OkUser\OkUser();
        $res = $user->firendList($parmas);
        \Response\OutputHelper::outPut($res);
    }

    public function firendAllAction()
    {
        $parmas = $this->getRequest()->getQuery();
        $user = new \OkUser\OkUser();
        $res = $user->firendAll($parmas);
        \Response\OutputHelper::outPut($res);
    }

    public function userListAction()
    {
        $parmas = $this->getRequest()->getQuery();
        $user = new \OkUser\OkUser();
        $res = $user->userList($parmas);
        \Response\OutputHelper::outPut($res);
    }

    public function addUserAction()
    {
        $parmas = $this->getRequest()->getQuery();
        $user = new \OkUser\OkUser();
        $res = $user->insertUser($parmas);
        \Response\OutputHelper::outPut($res);
    }

    public function groupsAction()
    {
        $user = new \OkUser\OkUser();
        $res = $user->groupsList();
        \Response\OutputHelper::outPut($res);
    }
}