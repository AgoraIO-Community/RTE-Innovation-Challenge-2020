<?php

namespace ZipkinOpenTracing\Tests\Unit;

use PHPUnit_Framework_TestCase;
use ZipkinOpenTracing\ScopeManager;
use OpenTracing\Span;

final class ScopeManagerTest extends PHPUnit_Framework_TestCase
{
    /**
     * @var ScopeManager
     */
    private $manager;

    protected function setUp()
    {
        $this->manager = new ScopeManager();
    }

    public function testAbleGetActiveScope()
    {
        $this->assertNull($this->manager->getActive());

        $span = $this->prophesize(Span::class)->reveal();
        $scope = $this->manager->activate($span, false);

        $this->assertEquals($scope, $this->manager->getActive());
    }

    public function testScopeClosingDeactivates()
    {
        $span = $this->prophesize(Span::class)->reveal();
        $scope = $this->manager->activate($span, false);

        $scope->close();

        $this->assertNull($this->manager->getActive());
    }
}
